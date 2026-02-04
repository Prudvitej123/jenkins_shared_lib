def baseUrl = "https://github.com/Prudvitej123/"
def repoName = reponame
def gitRepoUrl = baseUrl + repoName + ".git"

def jobName = reponame
def branchSpec = branch ?: "*/main,*/master"
def jenkinsfilePath = "Jenkinsfile"

pipelineJob(jobName) {

    properties {
        pipelineTriggers {
            triggers {
                pollSCM {
                    scmpoll_spec('*/1 * * * *')
                    ignorePostCommitHooks(true)
                }
            }
        }
    }

    logRotator {
        numToKeep(5)
    }

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url(gitRepoUrl)
                        credentials('github_credentials')
                    }
                    branches(branchName)
                    extensions {
                        cleanBeforeCheckout()
                    }
                }
            }
            scriptPath(jenkinsfilePath)
        }
    }
}
