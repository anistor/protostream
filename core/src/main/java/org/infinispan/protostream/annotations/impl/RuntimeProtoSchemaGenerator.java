package org.infinispan.protostream.annotations.impl;

import java.util.Set;

import org.infinispan.protostream.BaseMarshaller;
import org.infinispan.protostream.SerializationContext;
import org.infinispan.protostream.annotations.ProtoSchemaBuilder;
import org.infinispan.protostream.annotations.ProtoSchemaBuilderException;
import org.infinispan.protostream.annotations.impl.types.UnifiedTypeFactory;
import org.infinispan.protostream.annotations.impl.types.XClass;
import org.infinispan.protostream.descriptors.GenericDescriptor;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

/**
 * This class is not to be directly invoked by users. See {@link ProtoSchemaBuilder} instead.
 *
 * @author anistor@redhat.com
 * @since 3.0
 */
public final class RuntimeProtoSchemaGenerator extends BaseProtoSchemaGenerator {

   /**
    * Flag indicating if we are running in OSGI.
    */
   private static final boolean IS_OSGI_CONTEXT;

   static {
      boolean isOSGi = false;
      try {
         isOSGi = RuntimeProtoSchemaGenerator.class.getClassLoader() instanceof org.osgi.framework.BundleReference;
      } catch (NoClassDefFoundError ex) {
         // Ignore exception, we're not in OSGI
      }
      IS_OSGI_CONTEXT = isOSGi;
   }

   public RuntimeProtoSchemaGenerator(UnifiedTypeFactory typeFactory, SerializationContext serializationContext, String generator,
                                      String fileName, String packageName, Set<XClass> classes, boolean autoImportClasses) {
      super(typeFactory, serializationContext, generator, fileName, packageName, classes, autoImportClasses);
      if (classes.isEmpty()) {
         throw new ProtoSchemaBuilderException("At least one class must be specified");
      }
   }

   /**
    * Return an imported ProtoTypeMetadata implementation or null if it cannot be imported.
    */
   @Override
   protected ProtoTypeMetadata importProtoTypeMetadata(XClass javaType) {
      // check if this is already marshallable in current SerializationContext
      BaseMarshaller<?> marshaller;
      try {
         marshaller = serializationContext.getMarshaller(javaType.asClass());
      } catch (Exception e) {
         // ignore
         return null;
      }

      // this is an already known type, defined in another schema file that we'll just need to import; nothing gets generated for it
      GenericDescriptor descriptor = serializationContext.getDescriptorByName(marshaller.getTypeName());
      return new ImportedProtoTypeMetadata(descriptor, marshaller, javaType);
   }

   @Override
   protected AbstractMarshallerCodeGenerator makeCodeGenerator() {
      try {
         return new MarshallerByteCodeGenerator(typeFactory, packageName, getClassPool());
      } catch (NotFoundException e) {
         throw new ProtoSchemaBuilderException(e);
      }
   }

   private ClassPool getClassPool() {
      ClassLoader classLoader = getClass().getClassLoader();
      ClassPool cp = new ClassPool(ClassPool.getDefault()) {
         @Override
         public ClassLoader getClassLoader() {
            return IS_OSGI_CONTEXT ? classLoader : super.getClassLoader();
         }
      };
      for (XClass c : classes) {
         cp.appendClassPath(new ClassClassPath(c.asClass()));
      }
      cp.appendClassPath(new LoaderClassPath(classLoader));
      return cp;
   }
}