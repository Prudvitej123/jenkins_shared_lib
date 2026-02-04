import groovy.json.JsonSlurper

// ✅ Correct way to read JSON in Job DSL
def jsonText = readFileFromWorkspace('jenkins/input_jobs.json')
def config   = new JsonSlurper().parseText(jsonText)

// base GitHub URL (already contains username)
def baseUrl = config.baseUrl

config.repositories.each { repo ->

    def repoName        = repo.name
    def branchName      = repo.branch
    def jenkinsfilePath = repo.scriptPath

    // ✅ Correct repo URL
    def gitRepoUrl = "${baseUrl}${repoName}.git"

    // ✅ Safe Jenkins job name
    def jobName = repoName.replace('/', '-')

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
                            credentials('github_credentials') // 🔑 FIX
                        }
                        branches(branchName)
                        extensions {
                            cleanBeforeCheckout()
                        }
                    }
                }
                scriptPath(jenkinsfilePath)
                lightweight(true)
            }
        }
    }
}
