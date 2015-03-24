package org.commando.remote.http.receiver;

//
//========================================================================
//Copyright (c) 1995-2014 Mort Bay Consulting Pty. Ltd.
//------------------------------------------------------------------------
//All rights reserved. This program and the accompanying materials
//are made available under the terms of the Eclipse Public License v1.0
//and Apache License v2.0 which accompanies this distribution.
//
//  The Eclipse Public License is available at
//  http://www.eclipse.org/legal/epl-v10.html
//
//  The Apache License v2.0 is available at
//  http://www.opensource.org/licenses/apache2.0.php
//
//You may elect to redistribute this code under either of these licenses.
//========================================================================
//

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.commando.dispatcher.InVmDispatcher;
import org.commando.remote.receiver.CommandReceiver;
import org.commando.remote.receiver.DefaultCommandReceiver;
import org.commando.remote.serializer.Serializer;
import org.commando.testbase.dispatcher.TestDispatcherFactory;
import org.commando.xml.serializer.XmlSerializer;

@SuppressWarnings("serial")
public class TestHttpReceiverServlet extends AbstractHttpCommandReceiverServlet {

    @Override
    protected CommandReceiver initCommandReceiver(final ServletConfig config) throws ServletException {
        Serializer serializer = new XmlSerializer();
        InVmDispatcher dispatcher = TestDispatcherFactory.createTestInVmDispatcher();
        DefaultCommandReceiver commandReceiver = new DefaultCommandReceiver(serializer, dispatcher);
        return commandReceiver;
    }

}
