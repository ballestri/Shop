package shop.model;

public class Country {

    private String iso;
    private String prefix;

    public Country(String iso, String prefix) {
        this.iso = iso;
        this.prefix = prefix;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String mod) {
        this.prefix = prefix;
    }

}
