package org.thegalactic.dgraph.io;

/*
 * DGraphSerializerDot.java
 *
 * Copyright: 2010-2015 Karell Bertet, France
 * Copyright: 2015-2016 The Galactic Organization, France
 *
 * License: http://www.cecill.info/licences/Licence_CeCILL-B_V1-en.html CeCILL-B license
 *
 * This file is part of java-lattices.
 * You can redistribute it and/or modify it under the terms of the CeCILL-B license.
 */
import java.io.BufferedWriter;
import java.io.IOException;

import org.thegalactic.dgraph.DGraph;
import org.thegalactic.dgraph.Edge;
import org.thegalactic.dgraph.Node;
import org.thegalactic.io.Writer;

/**
 * This class defines the way for writing a graph to a text file.
 *
 * ![DGraphSerializerDot](DGraphSerializerDot.png)
 *
 * @uml
 *
 * DGraphSerializerDot.png
 *
 * !include resources/org/thegalactic/dgraph/io/DGraphSerializerDot.iuml
 * !include resources/org/thegalactic/io/Writer.iuml
 *
 * hide members
 * show DGraphSerializerDot members
 * class DGraphSerializerDot #LightCyan
 * title DGraphSerializerDot UML graph
 */
public final class DGraphSerializerDot implements Writer<DGraph> {

    /**
     * The singleton instance.
     */
    private static final DGraphSerializerDot INSTANCE = new DGraphSerializerDot();

    /**
     * Return the singleton instance of this class.
     *
     * @return the singleton instance
     */
    public static DGraphSerializerDot getInstance() {
        return INSTANCE;
    }

    /**
     * Register this class for writing .dot files.
     */
    public static void register() {
        DGraphIOFactory.getInstance().registerWriter(DGraphSerializerDot.getInstance(), "dot");
    }

    /**
     * This class is not designed to be publicly instantiated.
     */
    private DGraphSerializerDot() {
    }

    /**
     * Write a graph to a output stream.
     *
     * @param graph a graph to write
     * @param file a file
     *
     * @throws IOException When an IOException occurs
     */
    @Override
    public void write(final DGraph graph, final BufferedWriter file) throws IOException {
        final String newLine = System.getProperty("line.separator");

        file.write("digraph G {");
        file.write(newLine);
        file.write("Graph [rankdir=BT]");
        file.write(newLine);

        for (final Node node : graph.getNodes()) {
            file.write(String.valueOf(node.getIdentifier()));
            file.write(" [label=\"");
            file.write(node.toString().replace("\"", "\\\""));
            file.write("\"]");
            file.write(newLine);
        }

        for (final Edge edge : graph.getEdges()) {
            file.write(String.valueOf(edge.getSource().getIdentifier()));
            file.write("->");
            file.write(String.valueOf(edge.getTarget().getIdentifier()));
            if (edge.hasContent()) {
                file.write(" [label=\"");
                file.write(edge.getContent().toString().replace("\"", "\\\""));
                file.write("\"]");
            }
            file.write(newLine);
        }

        file.write("}");
    }
}
