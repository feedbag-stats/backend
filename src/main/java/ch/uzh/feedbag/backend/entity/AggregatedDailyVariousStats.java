package ch.uzh.feedbag.backend.entity;

public class AggregatedDailyVariousStats {

    private long buildDuration = 0;
    private double solutionSwitches = 0;
    private double packageSwitches = 0;
    private double buildCount = 0;
    private double testsFixed = 0;
    private double testsRun = 0;
    private double successfulTests = 0;
    private double commits = 0;
    private double numSessions = 0;
    private double numSessionsLongerThanTenMin = 0;
    private double totalSessionDurationMillis = 0;
    private long sessionDuration = 0;
    private double breaks = 0;
    private double filesEdited = 0;

    public AggregatedDailyVariousStats() {
    }

    public AggregatedDailyVariousStats(
            long buildDuration,
            double solutionSwitches,
            double packageSwitches,
            double buildCount,
            double testsFixed,
            double testsRun,
            double successfulTests,
            double commits,
            double numSessions,
            double numSessionsLongerThanTenMin,
            double totalSessionDurationMillis,
            long sessionDuration,
            double breaks,
            double filesEdited
    ) {
        this.buildDuration = buildDuration;
        this.packageSwitches = solutionSwitches;
        this.packageSwitches = packageSwitches;
        this.buildCount = buildCount;
        this.testsFixed = testsFixed;
        this.testsRun = testsRun;
        this.successfulTests = successfulTests;
        this.commits = commits;
        this.numSessions = numSessions;
        this.numSessionsLongerThanTenMin = numSessionsLongerThanTenMin;
        this.totalSessionDurationMillis = totalSessionDurationMillis;
        this.sessionDuration = sessionDuration;
        this.breaks = breaks;
        this.filesEdited = filesEdited;
    }

    public long getBuildDuration() {
        return buildDuration;
    }

    public void setBuildDuration(long buildDuration) {
        this.buildDuration = buildDuration;
    }

    public double getSolutionSwitches() {
        return solutionSwitches;
    }

    public void setSolutionSwitches(double solutionSwitches) {
        this.solutionSwitches = solutionSwitches;
    }

    public double getPackageSwitches() {
        return packageSwitches;
    }

    public void setPackageSwitches(double packageSwitches) {
        this.packageSwitches = packageSwitches;
    }

    public double getBuildCount() {
        return buildCount;
    }

    public void setBuildCount(double buildCount) {
        this.buildCount = buildCount;
    }

    public double getTestsFixed() {
        return testsFixed;
    }

    public void setTestsFixed(double testsFixed) {
        this.testsFixed = testsFixed;
    }

    public double getTestsRun() {
        return testsRun;
    }

    public void setTestsRun(double testsRun) {
        this.testsRun = testsRun;
    }

    public double getSuccessfulTests() {
        return successfulTests;
    }

    public void setSuccessfulTests(double successfulTests) {
        this.successfulTests = successfulTests;
    }

    public double getCommits() {
        return commits;
    }

    public void setCommits(double commits) {
        this.commits = commits;
    }

    public double getNumSessions() {
        return numSessions;
    }

    public void setNumSessions(double numSessions) {
        this.numSessions = numSessions;
    }

    public double getNumSessionsLongerThanTenMin() {
        return numSessionsLongerThanTenMin;
    }

    public void setNumSessionsLongerThanTenMin(double numSessionsLongerThanTenMin) {
        this.numSessionsLongerThanTenMin = numSessionsLongerThanTenMin;
    }

    public double getTotalSessionDurationMillis() {
        return totalSessionDurationMillis;
    }

    public void setTotalSessionDurationMillis(double totalSessionDurationMillis) {
        this.totalSessionDurationMillis = totalSessionDurationMillis;
    }

    public long getSessionDuration() {
        return sessionDuration;
    }

    public void setSessionDuration(long sessionDuration) {
        this.sessionDuration = sessionDuration;
    }

    public double getBreaks() {
        return breaks;
    }

    public void setBreaks(double breaks) {
        this.breaks = breaks;
    }

    public double getFilesEdited() {
        return filesEdited;
    }

    public void setFilesEdited(double filesEdited) {
        this.filesEdited = filesEdited;
    }
}
