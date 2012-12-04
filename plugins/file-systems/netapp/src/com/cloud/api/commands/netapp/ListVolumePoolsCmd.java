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

import org.apache.cloudstack.api.BaseCmd;
import org.apache.cloudstack.api.Implementation;
import org.apache.cloudstack.api.ServerApiException;
import com.cloud.api.response.ListResponse;
import com.cloud.exception.ConcurrentOperationException;
import com.cloud.exception.InsufficientCapacityException;
import com.cloud.exception.InvalidParameterValueException;
import com.cloud.exception.ResourceAllocationException;
import com.cloud.exception.ResourceUnavailableException;
import com.cloud.netapp.NetappManager;
import com.cloud.netapp.PoolVO;
import com.cloud.server.ManagementService;
import com.cloud.server.api.response.netapp.ListVolumePoolsCmdResponse;
import com.cloud.utils.component.ComponentLocator;

@Implementation(description="List Pool", responseObject = ListVolumePoolsCmdResponse.class)
public class ListVolumePoolsCmd extends BaseCmd {
	public static final Logger s_logger = Logger.getLogger(ListVolumePoolsCmd.class.getName());
    private static final String s_name = "listpoolresponse";


	@Override
	public void execute() throws ResourceUnavailableException,
			InsufficientCapacityException, ServerApiException,
			ConcurrentOperationException, ResourceAllocationException {
		ComponentLocator locator = ComponentLocator.getLocator(ManagementService.Name);
    	NetappManager netappMgr = locator.getManager(NetappManager.class);
    	try {
    		List<PoolVO> poolList = netappMgr.listPools();
    		ListResponse<ListVolumePoolsCmdResponse> listResponse = new ListResponse<ListVolumePoolsCmdResponse>();
    		List<ListVolumePoolsCmdResponse> responses = new ArrayList<ListVolumePoolsCmdResponse>();
    		for (PoolVO pool : poolList) {
    			ListVolumePoolsCmdResponse response = new ListVolumePoolsCmdResponse();
    			response.setId(pool.getId());
    			response.setName(pool.getName());
    			response.setAlgorithm(pool.getAlgorithm());
    			response.setObjectName("pool");
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
