#import "RNAppGlobals.h"

@implementation RNAppGlobals

@synthesize appBridge;

+ (id)sharedInstance {
    
    static RNAppGlobals *instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] init];
    });
    return instance;
}

@end
