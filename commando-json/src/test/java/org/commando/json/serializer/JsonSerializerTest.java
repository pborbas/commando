package org.commando.json.serializer;

import org.commando.command.Command;
import org.commando.example.SampleCommand;
import org.commando.example.SampleResult;
import org.commando.exception.CommandSerializationException;
import org.commando.json.serializer.JsonSerializer;
import org.commando.remote.serializer.Serializer;
import org.commando.result.Result;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 */
public class JsonSerializerTest {
	@Test
	public void testCommandInheritanceSerialization() throws CommandSerializationException {
		String data = "Őűúépú";
		SampleCommand sampleCommand = new SampleCommand();
		sampleCommand.setData(data);
		Serializer serializer = new JsonSerializer();
		String textCommand = serializer.toText(sampleCommand);
		System.out.println(textCommand);
		Command<?> command = serializer.toCommand(textCommand);
		Assert.assertEquals(sampleCommand.getClass(), command.getClass());
		Assert.assertTrue(command instanceof SampleCommand);
		Assert.assertEquals(data, ((SampleCommand) command).getData());
	}

	@Test
	@Ignore
	public void testCommandInheritanceSerializationSpeed() throws CommandSerializationException {
		String data = "Őűúépú";
		SampleCommand sampleCommand = new SampleCommand();
		sampleCommand.setData(data);
		Serializer serializer = new JsonSerializer();
		long start = System.currentTimeMillis();
		for (int i = 1; i < 100000; i++) {
			String textCommand = serializer.toText(sampleCommand);
			//	System.out.println(textCommand);
			Command<?> command = serializer.toCommand(textCommand);
			//			Assert.assertEquals(sampleCommand.getClass(), command.getClass());
			//			Assert.assertTrue(command instanceof SampleCommand);
			//			Assert.assertEquals(data, ((SampleCommand) command).getData());
		}
		System.out.println("JSON: " + (System.currentTimeMillis() - start));
	}

	@Test
	public void testResultInheritanceSerialization() throws CommandSerializationException {
		String data = "Őűúépú";
		SampleResult sampleResult = new SampleResult("1", data);
		Serializer serializer = new JsonSerializer();
		String textResult = serializer.toText(sampleResult);
		Result result = serializer.toResult(textResult);
		Assert.assertEquals(sampleResult.getClass(), result.getClass());
		Assert.assertEquals(data, ((SampleResult) result).getValue());
	}
}