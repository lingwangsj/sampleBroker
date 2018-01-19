/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { SampleBrokerTestModule } from '../../../test.module';
import { WlanMySuffixDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/wlan-my-suffix/wlan-my-suffix-delete-dialog.component';
import { WlanMySuffixService } from '../../../../../../main/webapp/app/entities/wlan-my-suffix/wlan-my-suffix.service';

describe('Component Tests', () => {

    describe('WlanMySuffix Management Delete Component', () => {
        let comp: WlanMySuffixDeleteDialogComponent;
        let fixture: ComponentFixture<WlanMySuffixDeleteDialogComponent>;
        let service: WlanMySuffixService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SampleBrokerTestModule],
                declarations: [WlanMySuffixDeleteDialogComponent],
                providers: [
                    WlanMySuffixService
                ]
            })
            .overrideTemplate(WlanMySuffixDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WlanMySuffixDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WlanMySuffixService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
