package org.infinispan.protostream.types.java.arrays;

import org.infinispan.protostream.annotations.ProtoAdapter;
import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoName;

/**
 * @author anistor@redhat.com
 * @since 4.4
 */
@ProtoAdapter(Integer[].class)
@ProtoName("BoxedIntegerArray")
public final class BoxedIntegerArrayAdapter extends AbstractArrayAdapter<Integer> {

   @ProtoFactory
   public Integer[] create(int size) {
      return new Integer[size];
   }
}
