/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.core.geometry;

import java.util.Hashtable;

import junit.framework.TestCase;

/**
 *
 * @author miles
 */
public class Vec2Test extends TestCase {
    
    public Vec2Test(String testName) {
        super(testName);
    }

    public void testHash() {
    	Hashtable h = new Hashtable();
    	Vec2 v0 = new Vec2(0,0);
    	Vec2 v1 = new Vec2(1,1);
    	Vec2 v2 = new Vec2(2,2);
    	Vec2 v3 = new Vec2(1,1);
    	
    	assertEquals( v1.hashCode(), v3.hashCode() );
    	assertNotSame( new Integer(v0.hashCode()), new Integer(v1.hashCode() ) );
    	
    	h.put( v0, v0);
    	h.put( v1, v1 );
    	
    	assertTrue( h.containsKey( v1 ) );
    	assertTrue( h.containsKey( v0 ));
    	assertTrue( h.containsKey( v3 ));
    	
    	assertEquals( v1, v3 );
    }
    
}
