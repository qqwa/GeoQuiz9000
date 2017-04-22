package no.ntnu.tdt4240.geoquiz9000.models;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Generated;

// Intended to be stored via ObjectBox
// Contains most necessary meta information about a map, before starting a game/map
@Entity
public class MapStore extends AbstractModel implements IMap {
    @Id
    private long id;


    private String name;
    private int mapType;
    protected String rootPath;
    protected int locationCount;

    public String getName() { return name; }
    public IMap.MapType getType() {
        return MapType.convert(mapType);
    }

    public String getRootPath() { return rootPath; }

    public int getLocationCount() { return locationCount; }


    @Generated(hash = 608205584)
    public MapStore(long id, String name, int mapType, String rootPath,
                    int locationCount) {
        this.id = id;
        this.name = name;
        this.mapType = mapType;
        this.rootPath = rootPath;
        this.locationCount = locationCount;
    }
    @Generated(hash = 2093851778)
    public MapStore() {
    }

    public MapStore(String name, int mapType, String rootPath, int locationCount) {
        this.name = name;
        this.mapType = mapType;
        this.rootPath = rootPath;
        this.locationCount = locationCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public void setLocationCount(int locationCount) {
        this.locationCount = locationCount;
    }
    public int getMapType() {
        return mapType;
    }
    public void setMapType(int mapType) {
        this.mapType = mapType;
    }
    public void setMapType(MapType mapType) { this.mapType = MapType.convert(mapType); }

}
