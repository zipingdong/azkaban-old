/*
 * Copyright 2017 LinkedIn Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package azkaban.jobExecutor;

import azkaban.utils.FileIOUtils;
import azkaban.utils.Props;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class HiveSqlJob extends ProcessJob {
    private static final Logger logger = Logger.getLogger(HiveSqlJob.class);

    //public static final String JDBC_URL_DRIVER = "hiveserver2.jdbc.url.driver";
    public static final String JDBC_URL_HOST = "hiveserver2.jdbc.url.host";
    public static final String JDBC_URL_PORT = "hiveserver2.jdbc.url.port";
    public static final String JDBC_URL_USERNAME = "hiveserver2.jdbc.url.username";
    public static final String JDBC_URL_PASSWORD = "hiveserver2.jdbc.url.password";
    //public static final String JDBC_URL_DATABASE = "hiveserver2.jdbc.url.database";

    private String BEELINE;

    public HiveSqlJob(final String jobId, final Props sysProps,
                      final Props jobProps, final Logger log) {
        super(jobId, sysProps, jobProps, log);

        BEELINE = "beeline -u jdbc:hive2://" + sysProps.getString(JDBC_URL_HOST) + ":" + sysProps.getString(JDBC_URL_PORT);
        if (StringUtils.isNotBlank(sysProps.getString(JDBC_URL_USERNAME))
                && StringUtils.isNotBlank(sysProps.getString(JDBC_URL_PASSWORD))) {
            BEELINE = BEELINE + " -n " + sysProps.getString(JDBC_URL_USERNAME)
                    + " -p " + sysProps.getString(JDBC_URL_PASSWORD);
        }

        logger.info("BEELINE");
        logger.info(BEELINE);
        logger.info("sql");
    }

    @Override
    public void run() throws Exception {
        this.jobProps.put(ProcessJob.COMMAND, BEELINE + " -e \""
                + FileIOUtils.readToString(getWorkingDirectory(), jobProps.getString(ProcessJob.COMMAND)) + "\"");

        super.run();
    }
}
