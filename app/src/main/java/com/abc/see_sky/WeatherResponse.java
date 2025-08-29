package com.abc.see_sky;

public class WeatherResponse {
    private Current current;
    private Location location;

    public Current getCurrent() {
        return current;
    }

    public Location getLocation() {
        return location;
    }

    public static class Current {
        private float temp_c;
        private int humidity;
        private float wind_mph;
        private Condition condition;

        public float getTempC() {
            return temp_c;
        }

        public int getHumidity() {
            return humidity;
        }

        public float getWindMph() {
            return wind_mph;
        }

        public Condition getCondition() {
            return condition;
        }

        public static class Condition {
            private String text;
            private String icon;

            public String getText() {
                return text;
            }

            public String getIcon() {
                return icon;
            }
        }
    }

    public static class Location {
        private String name;

        public String getName() {
            return name;
        }
    }
}

