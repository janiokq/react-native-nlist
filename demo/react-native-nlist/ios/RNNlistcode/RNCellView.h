#import <UIKit/UIKit.h>
#import "RNTableViewCell.h"

@class RNTableViewCell;

@interface RNCellView : UIView

@property (nonatomic) NSInteger row;
@property (nonatomic) NSInteger section;
@property (nonatomic) float componentHeight;
@property (nonatomic) float componentWidth;
@property (nonatomic, weak) UITableView *tableView;
@property (nonatomic, strong) RNTableViewCell *tableViewCell;

@end
