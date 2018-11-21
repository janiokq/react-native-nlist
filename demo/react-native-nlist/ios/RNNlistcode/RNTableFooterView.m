
#import "RNTableFooterView.h"

@implementation RNTableFooterView


-(void)setComponentHeight:(float)componentHeight {
    _componentHeight = componentHeight;
    if (componentHeight){
        _tableView.tableFooterView = self;
    }
}

-(void)setComponentWidth:(float)componentWidth {
    _componentWidth = componentWidth;
    if (componentWidth){
        _tableView.tableFooterView = self;
    }
}


@end
