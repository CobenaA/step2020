// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.data;

/** A user on a website. */
public final class User {

    private final String logoutRedirectUrl;
    private final String userEmail;
    private final String logoutUrl;
    private final String loginRedirectUrl;
    private final String loginUrl;

    public User(String logoutRedirectUrl, String userEmail, String logoutUrl) {
        this.logoutRedirectUrl = logoutRedirectUrl;
        this.userEmail = userEmail;
        this.logoutUrl = logoutUrl;
        this.loginRedirectUrl = null;
        this.loginUrl = null;
    }

    public User(String loginRedirectUrl, String loginUrl) {
        this.loginRedirectUrl = loginRedirectUrl;
        this.loginUrl = loginUrl;
        this.logoutRedirectUrl = null;
        this.userEmail = null;
        this.logoutUrl = null;
    }
}