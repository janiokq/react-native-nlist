#import <Foundation/Foundation.h>
#import "RNTableView.h"

@interface JSONDataSource : NSObject<RNTableViewDatasource>

-(id)initWithDictionary:(NSDictionary *)params;
-(id)initWithFilename:(NSString *)file filter:(NSString *)filter args:(NSArray *)args;
@property (nonatomic, strong) NSArray *sections;
@end
