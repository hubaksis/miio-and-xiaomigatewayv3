/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.miio.internal.handler;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.miio.internal.basic.BasicChannelTypeProvider;
import org.openhab.binding.miio.internal.basic.MiIoDatabaseWatchService;
import org.openhab.binding.miio.internal.cloud.CloudConnector;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.thing.binding.BridgeHandler;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.thing.binding.builder.BridgeBuilder;
import org.openhab.core.thing.type.ChannelTypeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openhab.binding.miio.internal.discovery.MiIoLumiSubdeviceDiscovery;
import org.openhab.binding.miio.internal.json.GatewayDevicesList;

import org.openhab.core.thing.binding.ThingHandlerService;
import java.util.Collections;
import java.util.Collection;

/**
 * The {@link MiIoGatewayHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Marcel Verpaalen - Initial contribution
 */
@NonNullByDefault
public class MiIoGatewayHandler extends MiIoBasicHandler implements BridgeHandler {

    private final Logger logger = LoggerFactory.getLogger(MiIoGatewayHandler.class);

    private @Nullable MiIoLumiSubdeviceDiscovery discoveryService;

    public MiIoGatewayHandler(Bridge thing, MiIoDatabaseWatchService miIoDatabaseWatchService,
            CloudConnector cloudConnector, ChannelTypeRegistry channelTypeRegistry,
            BasicChannelTypeProvider basicChannelTypeProvider) {
        super(thing, miIoDatabaseWatchService, cloudConnector, channelTypeRegistry, basicChannelTypeProvider);
    }

    @Override
    public Bridge getThing() {
        return (Bridge) super.getThing();
    }

    /**
     * Creates a bridge builder, which allows to modify the bridge. The method
     * {@link BaseThingHandler#updateThing(Thing)} must be called to persist the changes.
     *
     * @return {@link BridgeBuilder} which builds an exact copy of the bridge
     */
    @Override
    protected BridgeBuilder editThing() {
        return BridgeBuilder.create(thing.getThingTypeUID(), thing.getUID()).withBridge(thing.getBridgeUID())
                .withChannels(thing.getChannels()).withConfiguration(thing.getConfiguration())
                .withLabel(thing.getLabel()).withLocation(thing.getLocation()).withProperties(thing.getProperties());
    }

    @Override
    public void childHandlerInitialized(ThingHandler childHandler, Thing childThing) {
        logger.info("Child initialized : {}  {}", childThing.getUID(), childThing.getLabel());
        childDevices.put(childThing, (MiIoAbstractHandler) childHandler);
    }

    @Override
    public void childHandlerDisposed(ThingHandler childHandler, Thing childThing) {
        logger.info("Childhandler Disposed : {}  {}", childThing.getUID(), childThing.getLabel());
        childDevices.remove(childThing);
    }

    public @Nullable BridgeHandler getHandler() {
        return this;
    }

    public void setDiscoveryService(MiIoLumiSubdeviceDiscovery discoveryService) {
        this.discoveryService = discoveryService;
    }

    public void sendRequestForDeviceList(){
        sendMiIoRequestToGetDeviceList();        
    }

    @Override
    public Collection<Class<? extends ThingHandlerService>> getServices() {
        return Collections.singleton(MiIoLumiSubdeviceDiscovery.class);
    }

    @Override
    public void getDevicesListRequestCompleted(GatewayDevicesList deviceList){        
        if(discoveryService != null)
            discoveryService.createDisvoceryResult(deviceList);
    }    
}
