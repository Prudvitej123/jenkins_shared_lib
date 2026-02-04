def baseUrl = "https://github.com/Prudvitej123/"
def repoName = reponame
def gitRepoUrl = baseUrl + repoName + ".git"

def jobName = reponame
def branchSpec = "*/main,*/master"   // 🔥 FIXED
def jenkinsfilePath = "Jenkinsfile"

pipelineJob(jobName) {

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url(gitRepoUrl)
                        credentials('github_credentials')
                    }
                    branches(branchSpec)
                }
            }
            scriptPath(jenkinsfilePath)
        }
    }
}

