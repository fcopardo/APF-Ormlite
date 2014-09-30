/*
 * Copyright (c) 2014. Fco Pardo Baeza.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.grizzly.apf.Ormlite.Model;


import com.grizzly.apf.Definitions.DefinitionsStrategies;

/**
 * Created on 30-06-14.
 */
public class BaseWebCall extends BaseModel<String> {

    private String toCall;

    public BaseWebCall() {
        super(String.class);
        this.setStorageStrategy(DefinitionsStrategies.STRATEGY_DONT_STORE);
    }

    @Override
    public String getId() {
        return getToCall();
    }

    @Override
    public void setId(String id) {
        setToCall(id);
    }

    public String getToCall() {
        return toCall;
    }

    public void setToCall(String toCall) {
        this.toCall = toCall;
    }
}
