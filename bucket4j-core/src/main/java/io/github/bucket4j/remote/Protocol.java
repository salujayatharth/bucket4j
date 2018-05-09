/*
 *
 *   Copyright 2015-2017 Vladimir Bukhtoyarov
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.bucket4j.remote;

import io.github.bucket4j.BucketConfiguration;

public class Protocol {

    private static Protocol[] KNOWN_PROTOCOLS = new Protocol[] {
            new Protocol(1)
    };

    private static Protocol NEWEST = KNOWN_PROTOCOLS[KNOWN_PROTOCOLS.length - 1];

    private final int version;

    private Protocol(int version) {
        this.version = version;
    }

    public static Protocol newest() {
        return NEWEST;
    }

    public static Protocol forVersion(int version) {
        for (Protocol protocol : KNOWN_PROTOCOLS) {
            if (protocol.version == version) {
                return protocol;
            }
        }
        // TODO replace to specific exception
        throw new UnsupportedOperationException();
    }

    public String toJson(RemoteCommand<?> command) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public  <T> RemoteCommand<T> parseCommand(String commandAsJson) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String toJson(BucketConfiguration configuration) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public BucketConfiguration parseConfiguration(String configurationAsJson) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public <T> String toJson(CommandResult<T> result, RemoteCommand<T> command) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public <T> CommandResult<T> parseResult(String resultAsJson, RemoteCommand<T> command) {
        // TODO
        throw new UnsupportedOperationException();
    }

}
