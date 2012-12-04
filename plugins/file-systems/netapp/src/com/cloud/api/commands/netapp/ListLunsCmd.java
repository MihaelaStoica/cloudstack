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
package com.cloud.api.commands.netapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseCmd;
import org.apache.cloudstack.api.Implementation;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.ServerApiException;
import com.cloud.api.response.ListResponse;
import com.cloud.exception.ConcurrentOperationException;
import com.cloud.exception.InsufficientCapacityException;
import com.cloud.exception.InvalidParameterValueException;
import com.cloud.exception.ResourceAllocationException;
import com.cloud.exception.ResourceUnavailableException;
import com.cloud.netapp.LunVO;
import com.cloud.netapp.NetappManager;
import com.cloud.server.ManagementService;
import com.cloud.server.api.response.netapp.ListLunsCmdResponse;
import com.cloud.utils.component.ComponentLocator;

@Implementation(description="List LUN", responseObject = ListLunsCmdResponse.class)
public class ListLunsCmd extends BaseCmd 
{
	public static final Logger s_logger = Logger.getLogger(ListLunsCmd.class.getName());
    private static final String s_name = "listlunresponse";

    @Parameter(name=ApiConstants.POOL_NAME, type=CommandType.STRING, required = true, description="pool name.")
	private String poolName;

	@Override
	public void execute() throws ResourceUnavailableException,
			InsufficientCapacityException, ServerApiException,
			ConcurrentOperationException, ResourceAllocationException {
		ComponentLocator locator = ComponentLocator.getLocator(ManagementService.Name);
    	NetappManager netappMgr = locator.getManager(NetappManager.class);
    	try {
    		List<LunVO> lunList = netappMgr.listLunsOnFiler(poolName);
    		ListResponse<ListLunsCmdResponse> listResponse = new ListResponse<ListLunsCmdResponse>();
    		List<ListLunsCmdResponse> responses = new ArrayList<ListLunsCmdResponse>();
    		for (LunVO lun : lunList) {
    			ListLunsCmdResponse response = new ListLunsCmdResponse();
    			response.setId(lun.getId());
    			response.setIqn(lun.getTargetIqn());
    			response.setName(lun.getLunName());
    			response.setVolumeId(lun.getVolumeId());
    			response.setObjectName("lun");
    			responses.add(response);
    		}
    		listResponse.setResponses(responses);
    		listResponse.setResponseName(getCommandName());
    		this.setResponseObject(listResponse);
    	} catch (InvalidParameterValueException e) {
    		throw new ServerApiException(BaseCmd.PARAM_ERROR, e.toString());
    	}		
	}

	@Override
	public String getCommandName() {
		// TODO Auto-generated method stub
		return s_name;
	}

	@Override
	public long getEntityOwnerId() {
		// TODO Auto-generated method stub
		return 0;
	}
}
