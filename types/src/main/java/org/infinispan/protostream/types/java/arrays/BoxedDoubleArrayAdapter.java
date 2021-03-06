package org.infinispan.protostream.types.java.arrays;

import org.infinispan.protostream.annotations.ProtoAdapter;
import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoName;

/**
 * @author anistor@redhat.com
 * @since 4.4
 */
@ProtoAdapter(Double[].class)
@ProtoName("BoxedDoubleArray")
public final class BoxedDoubleArrayAdapter extends AbstractArrayAdapter<Double> {

   @ProtoFactory
   public Double[] create(int size) {
      return new Double[size];
   }
}
