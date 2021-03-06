package org.apache.maven.it.launcher;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/**
 * Launches an embedded Maven 3.x instance from the current class path, i.e. the Maven 3.x dependencies are assumed to
 * be present on the class path.
 *
 * @author Benjamin Bentmann
 * @deprecated Use {@link Embedded3xLauncher#createFromClasspath()}
 */
public class Classpath3xLauncher extends Embedded3xLauncher implements MavenLauncher {
  /**
   * @deprecated Use {@link Classpath3xLauncher#createFromClasspath()}
   */
  public Classpath3xLauncher(String mavenHome) throws LauncherException {
    super(createFromMavenHome(mavenHome, null, null));
  }
}
