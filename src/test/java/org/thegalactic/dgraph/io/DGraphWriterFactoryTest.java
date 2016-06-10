package org.thegalactic.dgraph.io;

/*
 * DGraphWriterFactoryTest.java
 *
 * Copyright: 2010-2015 Karell Bertet, France
 * Copyright: 2015-2016 The Galactic Organization, France
 *
 * License: http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html CeCILL-B license
 *
 * This file is part of java-lattices, free package. You can redistribute it and/or modify
 * it under the terms of CeCILL-B license.
 */

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Test the org.thegalactic.dgraph.io.DGraphWriterFactory class.
 */
public class DGraphWriterFactoryTest {
    /**
     * Test unregister.
     */
    @Test
    public void testUnregister() {
        DGraphWriterDot.register();
        DGraphWriter writer = DGraphWriterFactory.unregister("dot");
        assertTrue(writer instanceof DGraphWriter);
        assertEquals(DGraphWriterFactory.get("dot"), null);
    }
}
