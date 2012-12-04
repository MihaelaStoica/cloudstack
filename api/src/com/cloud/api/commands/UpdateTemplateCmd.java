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

import org.apache.cloudstack.api.BaseCmd;
import org.apache.cloudstack.api.Implementation;
import org.apache.cloudstack.api.ServerApiException;
import com.cloud.api.response.TemplateResponse;
import com.cloud.template.VirtualMachineTemplate;
import com.cloud.user.Account;

@Implementation(description="Updates attributes of a template.", responseObject=TemplateResponse.class)
public class UpdateTemplateCmd extends UpdateTemplateOrIsoCmd {
	public static final Logger s_logger = Logger.getLogger(UpdateTemplateCmd.class.getName());
    private static final String s_name = "updatetemplateresponse";

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////
    
    @Override
    public Boolean isBootable() {
        return null;
    }

    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////
    
    @Override
    public String getCommandName() {
        return s_name;
    }
    
    @SuppressWarnings("unchecked")
    public TemplateResponse getResponse() {
       return null;
    }
    
    @Override
    public long getEntityOwnerId() {
        VirtualMachineTemplate template = _entityMgr.findById(VirtualMachineTemplate.class, getId());
        if (template != null) {
            return template.getAccountId();
        }

        return Account.ACCOUNT_ID_SYSTEM; // no account info given, parent this command to SYSTEM so ERROR events are tracked
    }
    
    @Override
    public void execute(){
        VirtualMachineTemplate result = _mgr.updateTemplate(this);
        if (result != null) {
            TemplateResponse response = _responseGenerator.createIsoResponse(result);
            response.setObjectName("template");
            response.setResponseName(getCommandName());
            this.setResponseObject(response);
        } else {
            throw new ServerApiException(BaseCmd.INTERNAL_ERROR, "Failed to update template");
        }
    }
}
