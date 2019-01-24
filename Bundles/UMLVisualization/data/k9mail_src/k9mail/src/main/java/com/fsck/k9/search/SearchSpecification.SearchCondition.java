package com.fsck.k9.search;

import android.os.Parcel;
import android.os.Parcelable;
public class SearchCondition implements Parcelable {
        public final String value;
        public final Attribute attribute;
        public final SearchField field;

        public SearchCondition(SearchField field, Attribute attribute, String value) {
            this.value = value;
            this.attribute = attribute;
            this.field = field;
        }

        private SearchCondition(Parcel in) {
            this.value = in.readString();
            this.attribute = Attribute.values()[in.readInt()];
            this.field = SearchField.values()[in.readInt()];
        }

        @Override
        public SearchCondition clone() {
            return new SearchCondition(field, attribute, value);
        }

        public String toHumanString() {
            return field.toString() + attribute.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof SearchCondition) {
                SearchCondition tmp = (SearchCondition) o;
                if (tmp.attribute == attribute &&
                        tmp.field == field &&
                        tmp.value.equals(value)) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public int hashCode() {
            int result = 1;
            result = 31 * result + attribute.hashCode();
            result = 31 * result + field.hashCode();
            result = 31 * result + value.hashCode();

            return result;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(value);
            dest.writeInt(attribute.ordinal());
            dest.writeInt(field.ordinal());
        }

        public static final Parcelable.Creator<SearchCondition> CREATOR =
                new Parcelable.Creator<SearchCondition>() {

            @Override
            public SearchCondition createFromParcel(Parcel in) {
                return new SearchCondition(in);
            }

            @Override
            public SearchCondition[] newArray(int size) {
                return new SearchCondition[size];
            }
        };
    }