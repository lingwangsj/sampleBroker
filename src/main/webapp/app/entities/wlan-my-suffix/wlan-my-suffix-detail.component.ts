import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { WlanMySuffix } from './wlan-my-suffix.model';
import { WlanMySuffixService } from './wlan-my-suffix.service';

@Component({
    selector: 'jhi-wlan-my-suffix-detail',
    templateUrl: './wlan-my-suffix-detail.component.html'
})
export class WlanMySuffixDetailComponent implements OnInit, OnDestroy {

    wlan: WlanMySuffix;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private wlanService: WlanMySuffixService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInWlans();
    }

    load(id) {
        this.wlanService.find(id).subscribe((wlan) => {
            this.wlan = wlan;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInWlans() {
        this.eventSubscriber = this.eventManager.subscribe(
            'wlanListModification',
            (response) => this.load(this.wlan.id)
        );
    }
}
