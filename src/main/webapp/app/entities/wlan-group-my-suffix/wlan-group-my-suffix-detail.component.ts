import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { WlanGroupMySuffix } from './wlan-group-my-suffix.model';
import { WlanGroupMySuffixService } from './wlan-group-my-suffix.service';

@Component({
    selector: 'jhi-wlan-group-my-suffix-detail',
    templateUrl: './wlan-group-my-suffix-detail.component.html'
})
export class WlanGroupMySuffixDetailComponent implements OnInit, OnDestroy {

    wlanGroup: WlanGroupMySuffix;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private wlanGroupService: WlanGroupMySuffixService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInWlanGroups();
    }

    load(id) {
        this.wlanGroupService.find(id)
            .subscribe((wlanGroupResponse: HttpResponse<WlanGroupMySuffix>) => {
                this.wlanGroup = wlanGroupResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInWlanGroups() {
        this.eventSubscriber = this.eventManager.subscribe(
            'wlanGroupListModification',
            (response) => this.load(this.wlanGroup.id)
        );
    }
}
