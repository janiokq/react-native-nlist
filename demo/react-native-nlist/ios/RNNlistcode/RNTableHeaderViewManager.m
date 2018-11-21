
#import "RNTableHeaderViewManager.h"
#import "RNTableHeaderView.h"
@implementation RNTableHeaderViewManager

RCT_EXPORT_MODULE()
- (UIView *)view
{
    return [[RNTableHeaderView alloc] init];
}

RCT_EXPORT_VIEW_PROPERTY(componentHeight, float)
RCT_EXPORT_VIEW_PROPERTY(componentWidth, float)
@end
