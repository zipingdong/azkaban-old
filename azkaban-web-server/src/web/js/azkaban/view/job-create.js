$.namespace('azkaban');

var saveJobView;

azkaban.SaveJobView = Backbone.View
    .extend({
        events: {
            "click #save-job-btn": "handleSaveJob"
        },

        initialize: function (setting) {
            this.projectURL = contextURL + "manager";
            changeSelect()
        },

        handleSaveJob: function (evt) {
            var project_id = document.getElementById("project_id").value;
            var project_name = document.getElementById("project_name").value;
            var save_type = document.getElementById("save_type").value;
            var notify_user = document.getElementById("notify_user").value;

            // var job_command = document.getElementById("job_command").value;
            var job_command = editor.getValue().trim();

            var job_name = document.getElementById("job_name").value.trim();
            var job_type = document.getElementById("job_type").value.trim();
            var job_dependencies = document.getElementById("job_dependencies").value.trim();

            var job_warn_level = document.getElementById("job_warn_level").value.trim();
            var job_max_elapse = document.getElementById("job_max_elapse").value.trim();
            var job_retries = document.getElementById("job_retries").value.trim();
            var job_retries_interval = document.getElementById("job_retries_interval").value.trim();

            var job_proxy_user = document.getElementById("job_proxy_user").value.trim();

            if (job_name == null || $.trim(job_name) == "") {
                alert("作业名不可为空！");
                return;
            }
            if (job_type == null || $.trim(job_type) == "") {
                alert("作业类型不可为空！");
                return;
            }

            if (job_type == "hiveSql" && (job_command == null || job_command == "")) {
                alert("SQL不可为空！");
                return;
            }

            var jobOverride = {};
            jobOverride['project_id'] = project_id;
            jobOverride['project_name'] = project_name;

            jobOverride['save_type'] = save_type;

            jobOverride['job_name'] = job_name;
            jobOverride['job_type'] = job_type;
            jobOverride['job_command'] = job_command;
            jobOverride['job_dependencies'] = job_dependencies;

            jobOverride['job_warn_level'] = job_warn_level;
            jobOverride['job_max_elapse'] = job_max_elapse;
            jobOverride['job_retries'] = job_retries;
            jobOverride['job_retries_interval'] = job_retries_interval;

            jobOverride['notify_user'] = notify_user;

            jobOverride['job_proxy_user'] = job_proxy_user;

            var jobOverrideData = {
                project: project_name,
                ajax: "saveJob",
                jobOverride: jobOverride
            };

            var projectURL = this.projectURL
            var redirectURL = projectURL + '?project=' + project_name;
            var jobOverrideSuccessHandler = function (data) {
                if (data.error) {
                    alert(data.error);
                }
                else {
                    alert("保存作业成功！");
                    window.location = redirectURL;
                }
            };

            $.get(projectURL, jobOverrideData, jobOverrideSuccessHandler, "json");
        }
    });

$(function () {
    saveJobView = new azkaban.SaveJobView({
        el: $('#save-job-pane')
    });
});

function changeSelect() {
    var job_type = document.getElementById("job_type").value.trim();
    if (job_type == "hiveSql")
        $("#tr_job_command").show();
    else
        $("#tr_job_command").hide();
}
