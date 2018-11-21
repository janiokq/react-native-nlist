#import "RNCellView.h"

@implementation RNCellView


-(void)setTableView:(UITableView *)tableView {
    _tableView = tableView;
    _tableViewCell = [[RNTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"CustomCell"];
    _tableViewCell.cellView = self;
}

-(void)setComponentHeight:(float)componentHeight {
    _componentHeight = componentHeight;
    if (componentHeight){
        [_tableView reloadData];
    }
}


@end
