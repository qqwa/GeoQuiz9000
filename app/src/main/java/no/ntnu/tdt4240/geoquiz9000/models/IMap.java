package no.ntnu.tdt4240.geoquiz9000.models;

public interface IMap {
    public enum MapType {
        GOOGLE,
        PICTURE
    }
    public String getName();
    public MapType getType();
    public int locationsGetCount();
    public String locationsGetPicture(int id);
    public String locationsGetDescription(int id);
}