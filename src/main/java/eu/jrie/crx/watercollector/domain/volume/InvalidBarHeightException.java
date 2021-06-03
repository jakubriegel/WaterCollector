package eu.jrie.crx.watercollector.domain.volume;

import java.util.List;

public class InvalidBarHeightException extends Exception {

    private final List<Integer> surface;

    public InvalidBarHeightException(List<Integer> surface) {
        this.surface = surface;
    }

    public List<Integer> getSurface() {
        return surface;
    }
}
