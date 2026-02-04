import groovy.json.JsonSlurper

def jsonText = readFileFromWorkspace('jenkins/input_jobs.json')
def config   = new JsonSlurper().parseText(jsonText)

def baseUrl = config.baseUrl

config.repositories.each { repo ->

    def repoName        = repo.name
    def branchName      = repo.branch
    def jenkinsfilePath = repo.scriptPath

    def gitRepoUrl = "${baseUrl}${repoName}.git"
    def jobName    = repoName.replace('/', '-')

    pipelineJob(jobName) {

        properties {
            pipelineTriggers {
                triggers {
                    pollSCM {
                        scmpoll_spec('H/5 * * * *')
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
                            credentials('github_credentials') // ✅ WORKS
                        }
                        branches(branchName)
                        extensions {
                            cleanBeforeCheckout()
                        }
                    }
                }
                scriptPath(jenkinsfilePath)
                // ❌ lightweight REMOVED
            }
        }
    }
}
