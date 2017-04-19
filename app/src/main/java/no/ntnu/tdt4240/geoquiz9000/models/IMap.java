package no.ntnu.tdt4240.geoquiz9000.models;


public interface IMap {
    public static abstract class Location {};
    public enum MapType {
        GOOGLE,
        PICTURE;
        public static int convert(MapType mapType) {
            switch (mapType) {
                case GOOGLE:
                    return 1;
                case PICTURE:
                    return 2;
                default:
                    return -1;
            }
        }

        public static MapType convert(int mayType) throws EnumConstantNotPresentException {
            switch (mayType) {
                case 1:
                    return GOOGLE;
                case 2:
                    return PICTURE;
                default:
                    throw new EnumConstantNotPresentException(MapType.class, "No Type with Id " + mayType);
            }
        }
    }

    public String getName();
    public MapType getType();
    public String getRootPath();
    public int getLocationCount();
}
