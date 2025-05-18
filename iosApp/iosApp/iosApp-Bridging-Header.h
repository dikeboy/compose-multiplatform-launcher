// iosApp/iosApp-Bridging-Header.h
#import <Foundation/Foundation.h>

@interface SwiftHelper : NSObject
+ (NSString *)callSwiftMethod:(NSString *)param;
@end