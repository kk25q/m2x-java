package com.att.m2x.java;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import java.util.Date;
import java.util.Properties;
import java.util.UUID;

public abstract class M2XTestBase
{
	private static String masterKey;

	protected M2XClient client;
	protected String testId;
	protected Date now;

	@BeforeClass
	public static void classInit()
	{
		Properties props = new Properties();
		try
		{
			props.loadFromXML(ClassLoader.getSystemResourceAsStream("m2x.test.keys.xml"));
		}
		catch (Exception ex)
		{
			Assert.fail("Properties file not found");
		}
		if (!props.containsKey("key.master"))
		{
			Assert.fail("key.master not found");
		}
		masterKey = props.getProperty("key.master");
	}

	@Before
	public void testInit()
	{
		this.client = new M2XClient(masterKey);
		UUID id = UUID.randomUUID();
		this.testId = String.format("%1$X%2$X", id.getMostSignificantBits(), id.getLeastSignificantBits());
		this.now = new Date();
	}
}
