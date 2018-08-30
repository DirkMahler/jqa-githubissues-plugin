package org.jqassistant.contrib.plugin.githubissues.toolbox.cache;


import org.jqassistant.contrib.plugin.githubissues.ids.CommitId;
import org.jqassistant.contrib.plugin.githubissues.ids.RepositoryId;
import org.jqassistant.contrib.plugin.githubissues.jdom.XMLGitHubRepository;
import org.jqassistant.contrib.plugin.githubissues.json.*;
import org.jqassistant.contrib.plugin.githubissues.model.*;

import java.util.HashMap;
import java.util.Map;

/**
 * This class caches descriptor instances which have already been created.
 * <p>
 * For more information see {@link CacheEndpoint} which is the public accessible interface for this cache.
 */
class DescriptorCache {

    private HashMap<RepositoryId, GitHubRepository> repositories;
    private HashMap<CommitId, GitHubCommit> commits;
    private HashMap<String, GitHubIssue> issues;
    private HashMap<String, GitHubComment> comments;
    private HashMap<String, GitHubUser> users;
    private HashMap<String, GitHubLabel> labels;
    private HashMap<String, GitHubMilestone> milestones;

    DescriptorCache() {

        repositories = new HashMap<>();
        commits = new HashMap<>();
        issues = new HashMap<>();
        users = new HashMap<>();
        labels = new HashMap<>();
        milestones = new HashMap<>();
        comments = new HashMap<>();
    }

    GitHubMilestone get(JSONMilestone milestone, XMLGitHubRepository xmlGitHubRepository) {

        return milestones.get(xmlGitHubRepository.getUser() +
            "/" + xmlGitHubRepository.getName() +
            "#" + milestone.getNumber());
    }

    GitHubUser get(JSONUser user) {

        return users.get(user.getLogin());
    }

    GitHubLabel get(JSONLabel label) {

        return labels.get(label.getName());
    }

    GitHubRepository get(XMLGitHubRepository xmlGitHubRepository) {

        return repositories.get(new RepositoryId(xmlGitHubRepository));
    }

    /**
     * Matching commits is more complex than matching other entities:
     * <p>
     * Even if the GitHub-REST API is used to resolve markdown references the following issue can occur:
     * <p>
     * "kontext-e/jqassistant-plugins@a00cd018208b04caa08a32f970067cf8ec837eb8" and
     * "kontext-e/jqassistant-plugins@a00cd01" point at the same commit. Still, the GitHub API parses
     * them to different hrefs:
     * ../commit/a00cd018208b04caa08a32f970067cf8ec837eb8 and ../commit/a00cd01.
     * <p>
     * Therefore, we need to check if one of the commit hashes is a prefix of the other.
     *
     * @param repoUser  The repository user.
     * @param repoName  The repository name.
     * @param commitSha The commit hash.
     * @return The cached GitHubCommit or Null if it does not exist yet.
     */
    GitHubCommit getCommit(String repoUser, String repoName, String commitSha) {

        return commits.get(new CommitId(repoUser, repoName, commitSha));
    }

    GitHubIssue get(JSONIssue issue, XMLGitHubRepository xmlGitHubRepository) {

        return issues.get(xmlGitHubRepository.getUser() + "/" + xmlGitHubRepository.getName() + "#" + issue.getNumber());
    }

    GitHubComment get(JSONComment comment, XMLGitHubRepository xmlGitHubRepository) {

        return comments.get(xmlGitHubRepository.getUser() +
            "/" + xmlGitHubRepository.getName() +
            "/comment#" + comment.getId());
    }

    GitHubIssue getIssue(String repoUser, String repoName, String issueNumber) {

        return issues.get(repoUser + "/" + repoName + "#" + issueNumber);
    }

    void put(GitHubUser user) {

        if (!users.containsKey(user.getLogin())) {
            users.put(user.getLogin(), user);
        }
    }

    void put(GitHubMilestone milestone) {

        if (!milestones.containsKey(milestone.getMilestoneId())) {
            milestones.put(milestone.getMilestoneId(), milestone);
        }
    }

    void put(GitHubLabel label) {

        if (!labels.containsKey(label.getName())) {
            labels.put(label.getName(), label);
        }
    }

    void put(GitHubRepository repository) {

        RepositoryId key = new RepositoryId(repository);

        if (!repositories.containsKey(key)) {
            repositories.put(key, repository);
        }
    }

    void put(GitHubCommit commit) {

        if (!commits.containsKey(commit.getId())) {
            commits.put(commit.getId(), commit);
        }
    }

    void put(GitHubIssue issue) {

        if (!issues.containsKey(issue.getIssueId())) {
            issues.put(issue.getIssueId(), issue);
        }
    }

    void put(GitHubComment comment) {

        if (!comments.containsKey(comment.getCommentId())) {
            comments.put(comment.getCommentId(), comment);
        }
    }
}
