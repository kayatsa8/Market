package BusinessLayer.NotificationSystem.TestVersions;


/**
 * The package contains copy-versions of each class in NotificationSystem,
 * yet the NotificationHubTestVersion is not a Singleton as the original,
 * in order to fix the parallel tests problems.
 *
 * The rest of the objects will use the test version of the hub instead of the original as well.
 *
 * Any other functionality remained the same.
 */