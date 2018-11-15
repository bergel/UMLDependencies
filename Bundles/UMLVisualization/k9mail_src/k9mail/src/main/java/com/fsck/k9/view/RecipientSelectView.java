package com.fsck.k9.view;


import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import com.fsck.k9.R;
import com.fsck.k9.activity.AlternateRecipientAdapter;
import com.fsck.k9.activity.AlternateRecipientAdapter.AlternateRecipientListener;
import com.fsck.k9.activity.compose.RecipientAdapter;
import com.fsck.k9.activity.compose.RecipientLoader;
import com.fsck.k9.mail.Address;
import com.fsck.k9.view.RecipientSelectView.Recipient;
import com.tokenautocomplete.TokenCompleteTextView;
import org.apache.james.mime4j.util.CharsetUtil;
import timber.log.Timber;

import java.util.List;
public class RecipientSelectView extends TokenCompleteTextView<Recipient> implements LoaderCallbacks<List<Recipient>>,
        AlternateRecipientListener {

    private static final int MINIMUM_LENGTH_FOR_FILTERING = 2;

    private static final String ARG_QUERY = "query";

    private static final int LOADER_ID_FILTERING = 0;
    private static final int LOADER_ID_ALTERNATES = 1;


    private RecipientAdapter adapter;
    @Nullable
    private String cryptoProvider;
    private boolean showAdvancedInfo;
    private boolean showCryptoEnabled;
    @Nullable
    private LoaderManager loaderManager;

    private ListPopupWindow alternatesPopup;
    private AlternateRecipientAdapter alternatesAdapter;
    private Recipient alternatesPopupRecipient;
    private TokenListener<Recipient> listener;


    public RecipientSelectView(Context context) {
        super(context);
        initView(context);
    }

    public RecipientSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RecipientSelectView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        // TODO: validator?

        alternatesPopup = new ListPopupWindow(context);
        alternatesAdapter = new AlternateRecipientAdapter(context, this);
        alternatesPopup.setAdapter(alternatesAdapter);

        // don't allow duplicates, based on equality of recipient objects, which is e-mail addresses
        allowDuplicates(false);

        // if a token is completed, pick an entry based on best guess.
        // Note that we override performCompletion, so this doesn't actually do anything
        performBestGuess(true);

        adapter = new RecipientAdapter(context);
        setAdapter(adapter);

        setLongClickable(true);
    }

    @Override
    protected View getViewForObject(Recipient recipient) {
        View view = inflateLayout();

        RecipientTokenViewHolder holder = new RecipientTokenViewHolder(view);
        view.setTag(holder);

        bindObjectView(recipient, view);

        return view;
    }

    @SuppressLint("InflateParams")
    private View inflateLayout() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        return layoutInflater.inflate(R.layout.recipient_token_item, null, false);
    }

    private void bindObjectView(Recipient recipient, View view) {
        RecipientTokenViewHolder holder = (RecipientTokenViewHolder) view.getTag();

        holder.vName.setText(recipient.getDisplayNameOrAddress());

        RecipientAdapter.setContactPhotoOrPlaceholder(getContext(), holder.vContactPhoto, recipient);

        boolean hasCryptoProvider = cryptoProvider != null;
        if (!hasCryptoProvider) {
            holder.hideCryptoState();
            return;
        }

        boolean isAvailable = recipient.cryptoStatus == RecipientCryptoStatus.AVAILABLE_TRUSTED ||
                recipient.cryptoStatus == RecipientCryptoStatus.AVAILABLE_UNTRUSTED;
        if (!showAdvancedInfo) {
            holder.showSimpleCryptoState(isAvailable, showCryptoEnabled);
        } else {
            boolean isVerified = recipient.cryptoStatus == RecipientCryptoStatus.AVAILABLE_TRUSTED;
            holder.showAdvancedCryptoState(isAvailable, isVerified);
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        int action = event.getActionMasked();
        Editable text = getText();

        if (text != null && action == MotionEvent.ACTION_UP) {
            int offset = getOffsetForPosition(event.getX(), event.getY());

            if (offset != -1) {
                TokenImageSpan[] links = text.getSpans(offset, offset, RecipientTokenSpan.class);
                if (links.length > 0) {
                    showAlternates(links[0].getToken());
                    return true;
                }
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected Recipient defaultObject(String completionText) {
        Address[] parsedAddresses = Address.parse(completionText);
        if (!CharsetUtil.isASCII(completionText)) {
            setError(getContext().getString(R.string.recipient_error_non_ascii));
            return null;
        }
        if (parsedAddresses.length == 0 || parsedAddresses[0].getAddress() == null) {
            setError(getContext().getString(R.string.recipient_error_parse_failed));
            return null;
        }

        return new Recipient(parsedAddresses[0]);
    }

    public boolean isEmpty() {
        return getObjects().isEmpty();
    }

    public void setLoaderManager(@Nullable LoaderManager loaderManager) {
        this.loaderManager = loaderManager;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (loaderManager != null) {
            loaderManager.destroyLoader(LOADER_ID_ALTERNATES);
            loaderManager.destroyLoader(LOADER_ID_FILTERING);
            loaderManager = null;
        }
    }

    @Override
    public void onFocusChanged(boolean hasFocus, int direction, Rect previous) {
        super.onFocusChanged(hasFocus, direction, previous);
        if (hasFocus) {
            displayKeyboard();
        }
    }

    private void displayKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void showDropDown() {
        boolean cursorIsValid = adapter != null;
        if (!cursorIsValid) {
            return;
        }

        super.showDropDown();
    }

    @Override
    public void performCompletion() {
        if (getListSelection() == ListView.INVALID_POSITION && enoughToFilter()) {
            Object recipientText = defaultObject(currentCompletionText());
            if (recipientText != null) {
                replaceText(convertSelectionToString(recipientText));
            }
        } else {
            super.performCompletion();
        }
    }

    @Override
    protected void performFiltering(@NonNull CharSequence text, int start, int end, int keyCode) {
        if (loaderManager == null) {
            return;
        }

        String query = text.subSequence(start, end).toString();
        if (TextUtils.isEmpty(query) || query.length() < MINIMUM_LENGTH_FOR_FILTERING) {
            loaderManager.destroyLoader(LOADER_ID_FILTERING);
            return;
        }

        Bundle args = new Bundle();
        args.putString(ARG_QUERY, query);
        loaderManager.restartLoader(LOADER_ID_FILTERING, args, this);
    }

    public void setCryptoProvider(@Nullable String cryptoProvider, boolean showAdvancedInfo) {
        this.cryptoProvider = cryptoProvider;
        this.showAdvancedInfo = showAdvancedInfo;
        adapter.setShowAdvancedInfo(showAdvancedInfo);
        alternatesAdapter.setShowAdvancedInfo(showAdvancedInfo);
    }

    public void setShowCryptoEnabled(boolean showCryptoEnabled) {
        this.showCryptoEnabled = showCryptoEnabled;

        redrawAllTokens();
    }

    private void redrawAllTokens() {
        Editable text = getText();
        if (text == null) {
            return;
        }

        RecipientTokenSpan[] recipientSpans = text.getSpans(0, text.length(), RecipientTokenSpan.class);
        for (RecipientTokenSpan recipientSpan : recipientSpans) {
            bindObjectView(recipientSpan.getToken(), recipientSpan.view);
        }

        invalidate();
    }

    public void addRecipients(Recipient... recipients) {
        for (Recipient recipient : recipients) {
            addObject(recipient);
        }
    }

    public Address[] getAddresses() {
        List<Recipient> recipients = getObjects();
        Address[] address = new Address[recipients.size()];
        for (int i = 0; i < address.length; i++) {
            address[i] = recipients.get(i).address;
        }

        return address;
    }

    private void showAlternates(Recipient recipient) {
        if (loaderManager == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindowToken(), 0);

        alternatesPopupRecipient = recipient;
        loaderManager.restartLoader(LOADER_ID_ALTERNATES, null, RecipientSelectView.this);
    }

    public void postShowAlternatesPopup(final List<Recipient> data) {
        // We delay this call so the soft keyboard is gone by the time the popup is layouted
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                showAlternatesPopup(data);
            }
        });
    }

    public void showAlternatesPopup(List<Recipient> data) {
        if (loaderManager == null) {
            return;
        }

        // Copy anchor settings from the autocomplete dropdown
        View anchorView = getRootView().findViewById(getDropDownAnchor());
        alternatesPopup.setAnchorView(anchorView);
        alternatesPopup.setWidth(getDropDownWidth());

        alternatesAdapter.setCurrentRecipient(alternatesPopupRecipient);
        alternatesAdapter.setAlternateRecipientInfo(data);

        // Clear the checked item.
        alternatesPopup.show();
        ListView listView = alternatesPopup.getListView();
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        alternatesPopup.dismiss();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public Loader<List<Recipient>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID_FILTERING: {
                String query = args != null && args.containsKey(ARG_QUERY) ? args.getString(ARG_QUERY) : "";
                adapter.setHighlight(query);
                return new RecipientLoader(getContext(), cryptoProvider, query);
            }
            case LOADER_ID_ALTERNATES: {
                Uri contactLookupUri = alternatesPopupRecipient.getContactLookupUri();
                if (contactLookupUri != null) {
                    return new RecipientLoader(getContext(), cryptoProvider, contactLookupUri, true);
                } else {
                    return new RecipientLoader(getContext(), cryptoProvider, alternatesPopupRecipient.address);
                }
            }
        }

        throw new IllegalStateException("Unknown Loader ID: " + id);
    }

    @Override
    public void onLoadFinished(Loader<List<Recipient>> loader, List<Recipient> data) {
        if (loaderManager == null) {
            return;
        }

        switch (loader.getId()) {
            case LOADER_ID_FILTERING: {
                adapter.setRecipients(data);
                break;
            }
            case LOADER_ID_ALTERNATES: {
                postShowAlternatesPopup(data);
                loaderManager.destroyLoader(LOADER_ID_ALTERNATES);
                break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Recipient>> loader) {
        if (loader.getId() == LOADER_ID_FILTERING) {
            adapter.setHighlight(null);
            adapter.setRecipients(null);
        }
    }

    public boolean tryPerformCompletion() {
        if (!hasUncompletedText()) {
            return false;
        }
        int previousNumRecipients = getTokenCount();
        performCompletion();
        int numRecipients = getTokenCount();

        return previousNumRecipients != numRecipients;
    }

    private int getTokenCount() {
        return getObjects().size();
    }

    public boolean hasUncompletedText() {
        String currentCompletionText = currentCompletionText();
        return !TextUtils.isEmpty(currentCompletionText) && !isPlaceholderText(currentCompletionText);
    }

    static private boolean isPlaceholderText(String currentCompletionText) {
        // TODO string matching here is sort of a hack, but it's somewhat reliable and the info isn't easily available
        return currentCompletionText.startsWith("+") && currentCompletionText.substring(1).matches("[0-9]+");
    }

    @Override
    public void onRecipientRemove(Recipient currentRecipient) {
        alternatesPopup.dismiss();
        removeObject(currentRecipient);
    }

    @Override
    public void onRecipientChange(Recipient recipientToReplace, Recipient alternateAddress) {
        alternatesPopup.dismiss();

        List<Recipient> currentRecipients = getObjects();
        int indexOfRecipient = currentRecipients.indexOf(recipientToReplace);
        if (indexOfRecipient == -1) {
            Timber.e("Tried to refresh invalid view token!");
            return;
        }
        Recipient currentRecipient = currentRecipients.get(indexOfRecipient);

        currentRecipient.address = alternateAddress.address;
        currentRecipient.addressLabel = alternateAddress.addressLabel;
        currentRecipient.cryptoStatus = alternateAddress.cryptoStatus;

        View recipientTokenView = getTokenViewForRecipient(currentRecipient);
        if (recipientTokenView == null) {
            Timber.e("Tried to refresh invalid view token!");
            return;
        }

        bindObjectView(currentRecipient, recipientTokenView);

        if (listener != null) {
            listener.onTokenChanged(currentRecipient);
        }

        invalidate();
    }

    /**
     * This method builds the span given a recipient object. We override it with identical
     * functionality, but using the custom RecipientTokenSpan class which allows us to
     * retrieve the view for redrawing at a later point.
     */
    @Override
    protected TokenImageSpan buildSpanForObject(Recipient obj) {
        if (obj == null) {
            return null;
        }

        View tokenView = getViewForObject(obj);
        return new RecipientTokenSpan(tokenView, obj, (int) maxTextWidth());
    }

    /**
     * Find the token view tied to a given recipient. This method relies on spans to
     * be of the RecipientTokenSpan class, as created by the buildSpanForObject method.
     */
    private View getTokenViewForRecipient(Recipient currentRecipient) {
        Editable text = getText();
        if (text == null) {
            return null;
        }

        RecipientTokenSpan[] recipientSpans = text.getSpans(0, text.length(), RecipientTokenSpan.class);
        for (RecipientTokenSpan recipientSpan : recipientSpans) {
            if (recipientSpan.getToken().equals(currentRecipient)) {
                return recipientSpan.view;
            }
        }

        return null;
    }

    /**
     * We use a specialized version of TokenCompleteTextView.TokenListener as well,
     * adding a callback for onTokenChanged.
     */
    public void setTokenListener(TokenListener<Recipient> listener) {
        super.setTokenListener(listener);
        this.listener = listener;
    }


    

    

    

    

    
}