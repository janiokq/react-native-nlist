

#import "RNCellViewManager.h"
#import "RNCellView.h"

@implementation RNCellViewManager
RCT_EXPORT_MODULE()
- (UIView *)view
{
    return [[RNCellView alloc] init];
}

RCT_EXPORT_VIEW_PROPERTY(row, NSInteger)
RCT_EXPORT_VIEW_PROPERTY(section, NSInteger)
RCT_EXPORT_VIEW_PROPERTY(componentHeight, float)
RCT_EXPORT_VIEW_PROPERTY(componentWidth, float)

@end
