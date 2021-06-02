package eu.jrie.crx.watercollector.domain.volume.offset;

import eu.jrie.crx.watercollector.infra.volume.offset.ReverseIterator;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class OffsetRetriever {

    public Offset retrieveOffset(List<Integer> surface) {
        return new Offset(
                retrieveLeft(surface.iterator()),
                retrieveLeft(new ReverseIterator(surface))
        );
    }

    private static int retrieveLeft(Iterator<Integer> surface) {
        int offset = 0;
        int last = -1;
        while (surface.hasNext()){
            final int bar = surface.next();
            if (containerHasNotStarted(bar, last)) {
                offset++;
                last = bar;
            } else {
                break;
            }
        }
        return offset-1;
    }

    private static boolean containerHasNotStarted(final int bar, final int last) {
        return bar >= last;
    }
}
