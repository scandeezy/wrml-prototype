
package org.wrml;

public enum ResourceArchetype {

    DOCUMENT("Document", "document"),
    COLLECTION("Collection", "collection"),
    STORE("Store", "store"),
    CONTROLLER("Controller", "controller");

    private final String _Title;
    private final String _Keyword;

    private ResourceArchetype(String title, String keyword) {
        _Title = title;
        _Keyword = keyword;
    }

    public final String getKeyword() {
        return _Keyword;
    }

    public final String getTitle() {
        return _Title;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
