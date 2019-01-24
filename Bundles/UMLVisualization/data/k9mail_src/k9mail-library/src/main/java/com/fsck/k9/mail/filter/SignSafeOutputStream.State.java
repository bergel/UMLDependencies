package com.fsck.k9.mail.filter;


/**
 * Further encode a quoted-printable stream into a safer format for signed email.
 *
 * @see <a href="http://tools.ietf.org/html/rfc2015">RFC-2015</a>
 */
enum State {
        INIT {
            @Override
            public State nextState(int b) {
                switch (b) {
                    case '\r':
                        return lf_FROM;
                    default:
                        return INIT;

                }
            }
        },
        lf_FROM {
            @Override
            public State nextState(int b) {
                switch (b) {
                    case '\n':
                        return cr_FROM;
                    case '\r':
                        return lf_FROM;
                    default:
                        return INIT;

                }
            }
        },
        cr_FROM {
            @Override
            public State nextState(int b) {
                switch (b) {
                    case 'F':
                        return F_FROM;
                    case '\r':
                        return lf_FROM;
                    default:
                        return INIT;

                }
            }
        },
        F_FROM {
            @Override
            public State nextState(int b) {
                switch (b) {
                    case 'r':
                        return R_FROM;
                    case '\r':
                        return lf_FROM;
                    default:
                        return INIT;

                }
            }
        },
        R_FROM {
            @Override
            public State nextState(int b) {
                switch (b) {
                    case 'o':
                        return O_FROM;
                    case '\r':
                        return lf_FROM;
                    default:
                        return INIT;

                }
            }
        },
        O_FROM {
            @Override
            public State nextState(int b) {
                switch (b) {
                    case 'm':
                        return M_FROM;
                    case '\r':
                        return lf_FROM;
                    default:
                        return INIT;

                }
            }
        },
        M_FROM {
            @Override
            public State nextState(int b) {
                switch (b) {
                    case ' ':
                        return SPACE_FROM;
                    case '\r':
                        return lf_FROM;
                    default:
                        return INIT;

                }
            }

        },
        SPACE_FROM {
            @Override
            public State nextState(int b) {
                switch (b) {
                    case '\r':
                        return lf_FROM;
                    default:
                        return INIT;

                }
            }

        };

        public abstract State nextState(int b);
    }