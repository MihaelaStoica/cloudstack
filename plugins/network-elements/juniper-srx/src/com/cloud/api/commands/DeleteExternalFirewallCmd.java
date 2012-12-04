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
import org.apache.cloudstack.api.PlugService;
import org.apache.cloudstack.api.ServerApiException;
import com.cloud.api.response.SuccessResponse;
import com.cloud.exception.InvalidParameterValueException;
import com.cloud.network.element.JuniperSRXFirewallElementService;
import com.cloud.user.Account;

@Implementation(description="Deletes an external firewall appliance.", responseObject = SuccessResponse.class)
public class DeleteExternalFirewallCmd extends BaseCmd {
    public static final Logger s_logger = Logger.getLogger(DeleteExternalFirewallCmd.class.getName());    
    private static final String s_name = "deleteexternalfirewallresponse";    
    
    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @IdentityMapper(entityTableName="host")
    @Parameter(name=ApiConstants.ID, type=CommandType.LONG, required = true, description="Id of the external firewall appliance.")
    private Long id;
    
    ///////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////
     
    public Long getId() {
        return id;
    }
     
    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////

    @PlugService JuniperSRXFirewallElementService _srxElementService;

    @Override
    public String getCommandName() {
        return s_name;
    }
    
    @Override
    public long getEntityOwnerId() {
        return Account.ACCOUNT_ID_SYSTEM;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void execute(){
        try {
            boolean result = _srxElementService.deleteExternalFirewall(this);
            if (result) {
            SuccessResponse response = new SuccessResponse(getCommandName());
            response.setResponseName(getCommandName());
            this.setResponseObject(response);
            } else {
                throw new ServerApiException(BaseCmd.INTERNAL_ERROR, "Failed to delete external firewall.");
            }
        } catch (InvalidParameterValueException e) {
            throw new ServerApiException(BaseCmd.PARAM_ERROR, "Failed to delete external firewall.");
        }
    }
}
