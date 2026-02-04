def baseUrl = "https://github.com//"
def repoName = "$jobname"
def gitRepoUrl = baseUrl + repoName + ".git"
def jobName = "$reponame"

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
                scriptPath(jenkinsfilePath)   // ✅ now works
            }
        }
    }
}
