package com.englishlearning.android.jsonBean;

import java.util.List;

public class EnglishScore {

    /**
     * score : 9.2
     * detail_score : {"integrity":"10.0","segment":"9.0","fluency":"9.0","overall":"9.2"}
     * words : [{"score":"9.0","start":1680,"name":"i","end":1950,"mdd":"Correct","syllables":[{"score":"9.0","stress_dict":0,"name":"ay1","phones":[{"score":"9.0","rec":"ay","prob_rec":0.802,"dict":"ay","type":"Correct","prob_dict":0.831,"n_best_rec":[{"prob_rec":0.248,"name":"ao","index":1}]}]}]}]
     * TokenObject : {"english_level":"Intermediate","accent_type":"GB_or_US","feed_back_type":"MDD_Phone","user_id":"guest","for_child":false}
     * utter_info : {"intensity":{"min":0,"max":37.43,"mean":19.68},"ref_text":"[i:]","response_time":"2019-12-09 22:53:39","utterance_id":" ","api_version":"0.200.03.2019.09.25.11"}
     */

    private String score;
    private DetailScoreBean detail_score;
    private TokenObjectBean TokenObject;
    private UtterInfoBean utter_info;
    private List<WordsBean> words;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public DetailScoreBean getDetail_score() {
        return detail_score;
    }

    public void setDetail_score(DetailScoreBean detail_score) {
        this.detail_score = detail_score;
    }

    public TokenObjectBean getTokenObject() {
        return TokenObject;
    }

    public void setTokenObject(TokenObjectBean TokenObject) {
        this.TokenObject = TokenObject;
    }

    public UtterInfoBean getUtter_info() {
        return utter_info;
    }

    public void setUtter_info(UtterInfoBean utter_info) {
        this.utter_info = utter_info;
    }

    public List<WordsBean> getWords() {
        return words;
    }

    public void setWords(List<WordsBean> words) {
        this.words = words;
    }

    public static class DetailScoreBean {
        /**
         * integrity : 10.0
         * segment : 9.0
         * fluency : 9.0
         * overall : 9.2
         */

        private String integrity;
        private String segment;
        private String fluency;
        private String overall;

        public String getIntegrity() {
            return integrity;
        }

        public void setIntegrity(String integrity) {
            this.integrity = integrity;
        }

        public String getSegment() {
            return segment;
        }

        public void setSegment(String segment) {
            this.segment = segment;
        }

        public String getFluency() {
            return fluency;
        }

        public void setFluency(String fluency) {
            this.fluency = fluency;
        }

        public String getOverall() {
            return overall;
        }

        public void setOverall(String overall) {
            this.overall = overall;
        }
    }

    public static class TokenObjectBean {
        /**
         * english_level : Intermediate
         * accent_type : GB_or_US
         * feed_back_type : MDD_Phone
         * user_id : guest
         * for_child : false
         */

        private String english_level;
        private String accent_type;
        private String feed_back_type;
        private String user_id;
        private boolean for_child;

        public String getEnglish_level() {
            return english_level;
        }

        public void setEnglish_level(String english_level) {
            this.english_level = english_level;
        }

        public String getAccent_type() {
            return accent_type;
        }

        public void setAccent_type(String accent_type) {
            this.accent_type = accent_type;
        }

        public String getFeed_back_type() {
            return feed_back_type;
        }

        public void setFeed_back_type(String feed_back_type) {
            this.feed_back_type = feed_back_type;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public boolean isFor_child() {
            return for_child;
        }

        public void setFor_child(boolean for_child) {
            this.for_child = for_child;
        }
    }

    public static class UtterInfoBean {
        /**
         * intensity : {"min":0,"max":37.43,"mean":19.68}
         * ref_text : [i:]
         * response_time : 2019-12-09 22:53:39
         * utterance_id :
         * api_version : 0.200.03.2019.09.25.11
         */

        private IntensityBean intensity;
        private String ref_text;
        private String response_time;
        private String utterance_id;
        private String api_version;

        public IntensityBean getIntensity() {
            return intensity;
        }

        public void setIntensity(IntensityBean intensity) {
            this.intensity = intensity;
        }

        public String getRef_text() {
            return ref_text;
        }

        public void setRef_text(String ref_text) {
            this.ref_text = ref_text;
        }

        public String getResponse_time() {
            return response_time;
        }

        public void setResponse_time(String response_time) {
            this.response_time = response_time;
        }

        public String getUtterance_id() {
            return utterance_id;
        }

        public void setUtterance_id(String utterance_id) {
            this.utterance_id = utterance_id;
        }

        public String getApi_version() {
            return api_version;
        }

        public void setApi_version(String api_version) {
            this.api_version = api_version;
        }

        public static class IntensityBean {
            /**
             * min : 0
             * max : 37.43
             * mean : 19.68
             */

            private int min;
            private double max;
            private double mean;

            public int getMin() {
                return min;
            }

            public void setMin(int min) {
                this.min = min;
            }

            public double getMax() {
                return max;
            }

            public void setMax(double max) {
                this.max = max;
            }

            public double getMean() {
                return mean;
            }

            public void setMean(double mean) {
                this.mean = mean;
            }
        }
    }

    public static class WordsBean {
        /**
         * score : 9.0
         * start : 1680
         * name : i
         * end : 1950
         * mdd : Correct
         * syllables : [{"score":"9.0","stress_dict":0,"name":"ay1","phones":[{"score":"9.0","rec":"ay","prob_rec":0.802,"dict":"ay","type":"Correct","prob_dict":0.831,"n_best_rec":[{"prob_rec":0.248,"name":"ao","index":1}]}]}]
         */

        private String score;
        private int start;
        private String name;
        private int end;
        private String mdd;
        private List<SyllablesBean> syllables;

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public String getMdd() {
            return mdd;
        }

        public void setMdd(String mdd) {
            this.mdd = mdd;
        }

        public List<SyllablesBean> getSyllables() {
            return syllables;
        }

        public void setSyllables(List<SyllablesBean> syllables) {
            this.syllables = syllables;
        }

        public static class SyllablesBean {
            /**
             * score : 9.0
             * stress_dict : 0
             * name : ay1
             * phones : [{"score":"9.0","rec":"ay","prob_rec":0.802,"dict":"ay","type":"Correct","prob_dict":0.831,"n_best_rec":[{"prob_rec":0.248,"name":"ao","index":1}]}]
             */

            private String score;
            private int stress_dict;
            private String name;
            private List<PhonesBean> phones;

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
            }

            public int getStress_dict() {
                return stress_dict;
            }

            public void setStress_dict(int stress_dict) {
                this.stress_dict = stress_dict;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<PhonesBean> getPhones() {
                return phones;
            }

            public void setPhones(List<PhonesBean> phones) {
                this.phones = phones;
            }

            public static class PhonesBean {
                /**
                 * score : 9.0
                 * rec : ay
                 * prob_rec : 0.802
                 * dict : ay
                 * type : Correct
                 * prob_dict : 0.831
                 * n_best_rec : [{"prob_rec":0.248,"name":"ao","index":1}]
                 */

                private String score;
                private String rec;
                private double prob_rec;
                private String dict;
                private String type;
                private double prob_dict;
                private List<NBestRecBean> n_best_rec;

                public String getScore() {
                    return score;
                }

                public void setScore(String score) {
                    this.score = score;
                }

                public String getRec() {
                    return rec;
                }

                public void setRec(String rec) {
                    this.rec = rec;
                }

                public double getProb_rec() {
                    return prob_rec;
                }

                public void setProb_rec(double prob_rec) {
                    this.prob_rec = prob_rec;
                }

                public String getDict() {
                    return dict;
                }

                public void setDict(String dict) {
                    this.dict = dict;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public double getProb_dict() {
                    return prob_dict;
                }

                public void setProb_dict(double prob_dict) {
                    this.prob_dict = prob_dict;
                }

                public List<NBestRecBean> getN_best_rec() {
                    return n_best_rec;
                }

                public void setN_best_rec(List<NBestRecBean> n_best_rec) {
                    this.n_best_rec = n_best_rec;
                }

                public static class NBestRecBean {
                    /**
                     * prob_rec : 0.248
                     * name : ao
                     * index : 1
                     */

                    private double prob_rec;
                    private String name;
                    private int index;

                    public double getProb_rec() {
                        return prob_rec;
                    }

                    public void setProb_rec(double prob_rec) {
                        this.prob_rec = prob_rec;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public int getIndex() {
                        return index;
                    }

                    public void setIndex(int index) {
                        this.index = index;
                    }
                }
            }
        }
    }
}
