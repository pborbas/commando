package org.commando.xml.serializer;

import org.commando.command.Command;
import org.commando.example.SampleCommand;
import org.commando.example.SampleResult;
import org.commando.exception.CommandSerializationException;
import org.commando.remote.serializer.Serializer;
import org.commando.result.Result;
import org.junit.Assert;
import org.junit.Test;

public class XmlSerializerTest {

    @Test
    public void testCommandInheritanceSerialization() throws CommandSerializationException {
        SampleCommand sampleCommand = new SampleCommand();
        Serializer serializer = new XmlSerializer();
        String textCommand = serializer.toText(sampleCommand);
        Command<?> command = serializer.toCommand(textCommand);
        Assert.assertEquals(sampleCommand.getClass(), command.getClass());
        Assert.assertTrue(command instanceof SampleCommand);
    }

    @Test
    public void testResultInheritanceSerialization() throws CommandSerializationException {
        SampleResult sampleResult=new SampleResult("1");
        Serializer serializer = new XmlSerializer();
        String textResult = serializer.toText(sampleResult);
        Result result = serializer.toResult(textResult);
        Assert.assertEquals(sampleResult.getClass(), result.getClass());
    }
}