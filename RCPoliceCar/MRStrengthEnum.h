//
//  MRStrengthEnum.h
//  RCPoliceCar
//
//  Created by Andrei Vasilescu on 21/05/15.
//  Copyright (c) 2015 mready. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum {
    NONE,
    VERY_WEAK,
    WEAK,
    MEDIUM,
    GOOD,
    VERY_GOOD
} MRStrength;

@interface MRStrengthEnum : NSObject

+ (NSString *)imageForStrength:(MRStrength)strength;

@end
