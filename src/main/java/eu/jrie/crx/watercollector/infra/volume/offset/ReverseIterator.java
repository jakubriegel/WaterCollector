package eu.jrie.crx.watercollector.infra.volume.offset;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ReverseIterator implements Iterator<Integer> {

    private final ListIterator<Integer> listIterator;

    public ReverseIterator(List<Integer> list) {
        listIterator = list.listIterator(list.size());
    }

    @Override
    public boolean hasNext() {
        return listIterator.hasPrevious();
    }

    @Override
    public Integer next() {
        return listIterator.previous();
    }
}
