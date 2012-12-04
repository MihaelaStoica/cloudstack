// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package com.cloud.api.commands;

import org.apache.log4j.Logger;

import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseCmd;
import org.apache.cloudstack.api.IdentityMapper;
import org.apache.cloudstack.api.Implementation;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.ServerApiException;
import com.cloud.api.response.SuccessResponse;
import com.cloud.exception.ConcurrentOperationException;
import com.cloud.exception.InsufficientCapacityException;
import com.cloud.exception.InvalidParameterValueException;
import com.cloud.exception.ResourceAllocationException;
import com.cloud.exception.ResourceUnavailableException;
import com.cloud.network.ExternalNetworkDeviceManager;
import com.cloud.server.ManagementService;
import com.cloud.utils.component.ComponentLocator;
import com.cloud.utils.exception.CloudRuntimeException;

@Implementation(description="Deletes network device.", responseObject=SuccessResponse.class)
public class DeleteNetworkDeviceCmd extends BaseCmd {
	public static final Logger s_logger = Logger.getLogger(DeleteNetworkDeviceCmd.class);
	private static final String s_name = "deletenetworkdeviceresponse";
	
	/////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @IdentityMapper(entityTableName="host")
	@Parameter(name = ApiConstants.ID, type = CommandType.LONG, required=true, description = "Id of network device to delete")
    private Long id;
    
	
	public Long getId() {
		return id;
	}
	
	@Override
	public void execute() throws ResourceUnavailableException, InsufficientCapacityException, ServerApiException, ConcurrentOperationException,
			ResourceAllocationException {
		try {
			ExternalNetworkDeviceManager nwDeviceMgr;
			ComponentLocator locator = ComponentLocator.getLocator(ManagementService.Name);
			nwDeviceMgr = locator.getManager(ExternalNetworkDeviceManager.class);
			boolean result = nwDeviceMgr.deleteNetworkDevice(this);
			if (result) {
				SuccessResponse response = new SuccessResponse(getCommandName());
				response.setResponseName(getCommandName());
				this.setResponseObject(response);
			} else {
				throw new ServerApiException(BaseCmd.INTERNAL_ERROR, "Failed to delete network device:" + getId());
			}
		} catch (InvalidParameterValueException ipve) {
			throw new ServerApiException(BaseCmd.PARAM_ERROR, ipve.getMessage());
		} catch (CloudRuntimeException cre) {
			throw new ServerApiException(BaseCmd.INTERNAL_ERROR, cre.getMessage());
		}

	}

	@Override
	public String getCommandName() {
		return s_name;
	}

	@Override
	public long getEntityOwnerId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
