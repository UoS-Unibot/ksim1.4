package org.evors.core;

public class Queue extends java.util.Vector {

    /** Creates new Queue */
    public Queue () {
    }

    public void enq( Object o )
    {
        insertElementAt( o, 0 );
    }
    
    public Object deq()
    {
        return remove( size() - 1 );
    }
}