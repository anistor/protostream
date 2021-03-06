package org.infinispan.protostream.types.java.arrays;

import org.infinispan.protostream.annotations.ProtoAdapter;
import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoName;

/**
 * @author anistor@redhat.com
 * @since 4.4
 */
@ProtoAdapter(Long[].class)
@ProtoName("BoxedLongArray")
public final class BoxedLongArrayAdapter extends AbstractArrayAdapter<Long> {

   @ProtoFactory
   public Long[] create(int size) {
      return new Long[size];
   }
}
